package com.fooapp.cart.conf.log;

import com.internetitem.logback.elasticsearch.ClassicElasticsearchPublisher;
import com.internetitem.logback.elasticsearch.ElasticsearchAppender;

import java.io.IOException;

public class StructuredArgumentsElasticsearchAppender extends ElasticsearchAppender {

    protected ClassicElasticsearchPublisher buildElasticsearchPublisher() throws IOException {
        return new StructuredArgumentsElasticsearchPublisher(this.getContext(), this.errorReporter, this.settings, this.elasticsearchProperties, this.headers);
    }
}
