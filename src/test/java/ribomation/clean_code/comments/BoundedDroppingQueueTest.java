package ribomation.clean_code.comments;

// --- Revisions ----
// 2015-06-26 (ribomation) Fixed missing default param
// 2015-06-19 (ribomation) updated its distribution repo url
// 2015-06-05 (ribomation) commit after merge
// 2015-06-05 (Jens Riboe) final commit before merge into master
// 2015-06-05 (ribomation) final commit before merge into master
// 2015-05-29 (ribomation) Added support for setting the X-Load-Impact_Agent request header from jenkins plugin
// 2015-05-29 (ribomation) enabled exec bit on gradlew
// 2015-05-29 (ribomation) minor editorial edits
// 2015-05-29 (ribomation) Added X-Load-Impact_Agent request header showing the sdk version
// 2015-05-29 (ribomation) Added build-data properties file bundled in class-path. Added agent version req header.
// 2015-05-27 (ribomation) minor updates
// 2015-05-26 (ribomation) Updated the docs
// 2015-05-26 (ribomation) tracked down the problem with progress-result.
// 2015-05-25 (ribomation) Updated README with gradle instructions
// 2015-05-24 (ribomation) Completed the gradle build migration. Added integration-test CLI configurations. Added a check-my-account app intended to check the fatjar.
// 2015-05-23 (ribomation) added the wrapper
// 2015-05-21 (ribomation) Added Gradle build. Refactored the source organization. Added separate integrationTest task. Refined the running-load tests.
// 2015-05-20 (ribomation) Updated LoadZone to better handle city names with spaces and non-ascii letters
// 2015-05-20 (ribomation) Added integration-test for running a load-test. All integration-tests implemented so far, now succeeds.
// 2015-05-18 (ribomation) Enabled all integration tests to run by 'mvn test'
// 2015-05-15 (ribomation) Added integration test for usage of starting/aborting a load-test
// 2015-05-15 (ribomation) very minor refactoring of some unit tests and its test-data
// 2015-05-15 (ribomation) Added integration test for usage of test-configurations
// 2015-05-15 (ribomation) Added integration tests for:  token validation  load-zone usage  data-store usage  scenario usage
// 2015-05-11 (ribomation) Investigation and initial bugfix. still investigating more response problems
// 2014-05-14 (vagrant) [maven-release-plugin] prepare for next development iteration
// 2014-05-14 (Robin Gustafsson) Fix pom merge conflict.
// 2014-05-14 (vagrant) [maven-release-plugin] prepare release Load-Impact-Java-SDK-1.2
// 2014-05-14 (Robin Gustafsson) Update SDK version in README.
// 2014-05-07 (Robin Gustafsson) Fix indentation in README.

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

/**
 * Unit test of class BoundedDroppingQueue&lt;T&gt;
 *
 * @author jens
 * @date 2013-10-06, 09:13
 */
public class BoundedDroppingQueueTest {
    private final int N = 3;
    private BoundedDroppingQueue<Integer> target;

    @Before
    public void setUp() throws Exception {
        target = new BoundedDroppingQueue<Integer>(N);
    }

    //test 1
    @Test
    public void a_few_put_and_get_should_pass() throws Exception {
        assertThat(target.size(), is(0));
        assertThat(target.empty(), is(true));

        for (int k = 1; k <= N; ++k) target.put(k);
        assertThat(target.full(), is(true));

        for (int k = 1; k <= N; ++k) {
            int x = target.get();
            assertThat(x, is(k));
        }
        assertThat(target.size(), is(0));
        assertThat(target.empty(), is(true));
    }

    //test 2
    @Test
    public void more_put_than_size_should_pass() throws Exception {
        for (int k = 1; k <= N * 2; ++k) target.put(k);
        assertThat(target.size(), is(N));

        int k = N + 1;
        while (!target.empty()) {
            int n = target.get();
            assertThat(n, is(k++));
        }
    }

    //test 3
    @Test(expected = IllegalArgumentException.class)
    public void get_on_empty_should_fail() {
        target.put(1);
        target.get();
        target.get();
    }

    //test 4
    @Test
    public void queue_size_1_should_pass() {
        target = new BoundedDroppingQueue<Integer>(1);
        assertThat(target.size(), is(0));

        int M = 100;
        for (int k = 1; k <= M; ++k) {
            target.put(k);
            assertThat(target.size(), is(1));
        }
        assertThat(target.get(), is(M));
        assertThat(target.size(), is(0));
    }

    //test 5
    @Test
    public void foreach_loop_should_pass() {
        for (int k = 1; k <= N + 1; ++k) target.put(k);
        assertThat(target.size(), is(N));

        int k = 2;
        for (Integer n : target) assertThat(n, is(k++));
        assertThat(target.size(), is(N));
    }

    //test 6
    @Test
    public void toList_should_pass() {
        for (int k = 1; k <= N * 2; ++k) target.put(k);
        assertThat(target.size(), is(N));

        List<Integer> lst = target.toList();
        assertThat(lst, notNullValue());
        assertThat(lst.size(), is(target.size()));
        assertThat(lst.size(), is(N));

        int k = N + 1;
        for (Integer n : lst) {
            assertThat(n, is(k++));
        }
    }

}