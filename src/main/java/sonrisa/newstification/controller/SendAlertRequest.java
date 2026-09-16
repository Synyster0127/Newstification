package sonrisa.newstification.controller;

import sonrisa.newstification.channel.ChannelType;

public record SendAlertRequest(ChannelType channel, String newsTitle, String newsBody) {}