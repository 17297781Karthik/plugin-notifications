package org.kestra.task.notifications.slack;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableMap;
import com.google.common.io.Files;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import org.kestra.core.runners.RunContext;
import org.kestra.core.runners.RunContextFactory;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;
import javax.inject.Inject;

@MicronautTest
class SlackIncomingWebhookTest {
    @Inject
    private RunContextFactory runContextFactory;

    @Test
    void run() throws Exception {
        RunContext runContext = runContextFactory.of(ImmutableMap.of(
            "blocks", Arrays.asList(
                ImmutableMap.of(
                    "text", "A message *with some bold text* and _some italicized text_.",
                    "fields", Arrays.asList("*Priority*", "*Type*", "`High`", "`Unit Test`")
                ),
                ImmutableMap.of(
                    "text", "his is a mrkdwn section block :ghost: *this is bold*, and ~this is crossed out~, and <https://google.com|this is a link>",
                    "fields", Arrays.asList("*Priority*", "*Type*", "`Low`", "`Unit Test`")
                )
            )
        ));

        SlackIncomingWebhook task = SlackIncomingWebhook.builder()
            .url("http://www.mocky.io/v2/5dfa3bfd3600007dafbd6b91")
            .payload(
                Files.asCharSource(
                    new File(Objects.requireNonNull(SlackIncomingWebhookTest.class.getClassLoader()
                        .getResource("slack.hbs"))
                        .toURI()),
                    Charsets.UTF_8
                ).read()
            )
            .build();

        task.run(runContext);
    }
}
