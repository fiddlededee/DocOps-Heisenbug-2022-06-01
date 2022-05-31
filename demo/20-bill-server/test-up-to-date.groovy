@Grapes([
    @Grab(group='org.junit.jupiter', module='junit-jupiter-params', version='5.4.0'),
    @Grab(group = 'org.assertj', module = 'assertj-core', version = '3.22.0'),
    @Grab(group='org.asciidoctor', module='asciidoctorj', version='2.5.3'),
    @Grab(group='com.approvaltests', module='approvaltests', version='14.0.0'),
    @Grab(group='commons-io', module='commons-io', version='2.11.0')
])

import org.codehaus.groovy.runtime.StackTraceUtils
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.apache.commons.io.FilenameUtils

import org.junit.jupiter.api.BeforeAll
import org.junit.platform.launcher.listeners.LoggingListener
import java.util.logging.ConsoleHandler
import org.junit.jupiter.api.TestReporter;

import java.util.logging.Level
import java.util.logging.Logger;
import java.util.stream.Stream
import org.asciidoctor.Asciidoctor
import org.asciidoctor.SafeMode
import org.asciidoctor.Options
import org.approvaltests.Approvals
import org.approvaltests.namer.NamerFactory
import org.approvaltests.namer.NamedEnvironment


class TestActual {

  @BeforeAll
  static void init() {
    File actualTestAdocFile = new File("actual-test-adoc.adoc")
    actualTestAdocFile.write("")
    def logger = Logger.getLogger(LoggingListener.name)
    logger.level = Level.OFF
    logger.addHandler(new ConsoleHandler(level: Level.OFF))
  }

  static String toKebabCase( String text ) {
    text.replaceAll( /([A-Z])/, /-$1/ ).toLowerCase().replaceAll( /^_/, '-' )
  }

  static getCurrentMethodName(){
    def marker = new Throwable()
    String methodName = "";
    StackTraceUtils.sanitize(marker).stackTrace.collect {
      el -> el.methodName
    }.collate(2,1).each {
      el -> if (el[0] == 'getCurrentMethodName') {
        methodName = el[1]
      };
    }
    return methodName
  }

  static outputAttr (attrName) {
    File actualTestAdocFile = new File("actual-test-adoc.adoc")
    actualTestAdocFile.append(":$attrName:\n")
    println ":$attrName:"
  }

  private static Stream<String> provideFiles() {
    File dh = new File('./src-partials')
    def fl = []
    dh.eachFile {
      fl << it.toString()
    }
    return Stream.of(fl.toArray() as String[]);
  }

  @ParameterizedTest
  @MethodSource("provideFiles")
  void checkSrcPartials (String filename, TestReporter reporter) {
    String baseFilename = FilenameUtils.getBaseName(filename)
    Asciidoctor asciidoctor = Asciidoctor.Factory.create();
    Options options = Options.builder()
        .safe(SafeMode.UNSAFE)
        .baseDir(new File("./src-partials"))
        .build();
    try (NamedEnvironment en = NamerFactory.withParameters(baseFilename)) {
      File file = new File(filename)
      Approvals.verifyAll(file.getName()
          , asciidoctor.convert(file.text, options).replaceAll("(?s)<[^>]*>(\\s*<[^>]*>)*", " "));
      outputAttr(String.format("%s-%s", toKebabCase(getCurrentMethodName()), baseFilename))
    }
  }
}

