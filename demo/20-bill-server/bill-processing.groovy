@Grapes([
    @Grab(group='org.junit.jupiter', module='junit-jupiter-params', version='5.4.0'),
    @Grab(group='org.hamcrest', module='hamcrest-library', version='2.2'),
])

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

import java.util.stream.Stream

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;

class TestBillProcessor {
  BillProcessor billProcessor;
  TestBillProcessor () {
    billProcessor = new BillProcessor()
  }

  // tag::correct-transition-provider[]
  private static Stream<String> provideCorrectTransitions() {
    def doc = """ tag::correct-transitions[]
      draft --> pending
      pending --> approved
      pending --> rejected
      end::correct-transitions[] """
    return Stream.of(doc.split('\n')[1..-2] as String[]);
  }
  // end::correct-transition-provider[]

  // tag::processing-follows-model[]
  @ParameterizedTest
  @MethodSource("provideCorrectTransitions")
  void ProcessingFollowsModel(String correctTransition) {

    def transition = correctTransition.split('-->').collect { it.trim() };
    billProcessor.setState(transition[0])
    assertThat(Arrays.asList(billProcessor.getPossibleStates() as String[])
        , hasItems(transition[1]))
  }
  // end::processing-follows-model[]
}


class BillProcessor {
  String state
  BillProcessor () {
    setState('draft');
  }
  public setState(stateTo) {
    state = stateTo;
  }

  // tag::bill-process-implementation[]
  public getPossibleStates() {
    // tag::pres[]
    switch (state) {
      case 'draft':
        return ['pending'];
      case 'pending':
        return ['approved', 'rejected'];
    }
    return [];
    // end::pres[]
  }
  // end::bill-process-implementation[]

}

