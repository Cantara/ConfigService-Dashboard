package no.cantara.csdb.cs_client.commands;

import no.cantara.csdb.util.basecommands.BaseGetCommand;

/**
 * Placeholder command, reusing the ClientStatus call
 * <p>
 * http://stackoverflow.com/questions/31817483/how-to-fetch-logs-aws-vpc-logs-from-aws-which-are-seen-on-cloudwatch
 */
public class CommandGetAWSCloudWatchLog extends BaseGetCommand<String> {

    private String clientId;

    public CommandGetAWSCloudWatchLog(String clientId) {
        this.clientId = clientId;
    }

    @Override
    protected String getCacheKey() {
        return String.valueOf(clientId);
    }

    @Override
    protected String getTargetPath() {
        return "client/" + clientId + "/status";
    }


}

