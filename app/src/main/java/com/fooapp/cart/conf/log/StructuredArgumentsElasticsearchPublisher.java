package com.fooapp.cart.conf.log;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Context;
import com.fasterxml.jackson.core.JsonGenerator;
import com.internetitem.logback.elasticsearch.ClassicElasticsearchPublisher;
import com.internetitem.logback.elasticsearch.config.ElasticsearchProperties;
import com.internetitem.logback.elasticsearch.config.HttpRequestHeaders;
import com.internetitem.logback.elasticsearch.config.Settings;
import com.internetitem.logback.elasticsearch.util.ErrorReporter;
import net.logstash.logback.marker.ObjectAppendingMarker;

import java.io.IOException;

public class StructuredArgumentsElasticsearchPublisher extends ClassicElasticsearchPublisher {

    public StructuredArgumentsElasticsearchPublisher(Context context, ErrorReporter errorReporter, Settings settings, ElasticsearchProperties properties, HttpRequestHeaders headers) throws IOException {
        super(context, errorReporter, settings, properties, headers);
    }

    protected void serializeCommonFields(JsonGenerator gen, ILoggingEvent event) throws IOException {
        super.serializeCommonFields(gen, event);
        if(null != event && null != event.getArgumentArray())
        for (Object argument : event.getArgumentArray()) {
            if(argument instanceof ObjectAppendingMarker) {
                gen.writeObjectField(((ObjectAppendingMarker) argument).getFieldName(), argument.toString());
            }
        }
    }
}
