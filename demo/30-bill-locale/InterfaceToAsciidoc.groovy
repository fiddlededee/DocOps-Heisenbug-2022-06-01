import groovy.json.JsonSlurper

String i18nStringContents = new File('../../demo-bills/src/locales/en.json').text
sep = "-"

def jsonSlurper = new JsonSlurper()
def i18nJson = jsonSlurper.parseText i18nStringContents

attributes = []
loopJson i18nJson, "t"

new File('.','en.adoc').withWriter('utf-8') {
 writer -> writer.writeLine attributes.join("\n")
}

public void loopJson(json, fullKey) {
    json.each {key, value ->
        newKey = "$fullKey$sep$key"
        if (value instanceof String) {
           preparedValue = value.replaceAll(/\{/, "\\\\{")
          attributes << ":$newKey: $preparedValue"
       } else {
           childsCount = value.size()
           attributes << ":$newKey-childs-count-$childsCount:"
           loopJson value, newKey
       }
   }
}