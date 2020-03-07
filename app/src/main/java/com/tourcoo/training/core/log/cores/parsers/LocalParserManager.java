package com.tourcoo.training.core.log.cores.parsers;


import com.tourcoo.training.core.log.cores.IParser;


public class LocalParserManager {

    // 默认支持解析库
    @SuppressWarnings("unchecked")
    public static final Class<? extends IParser>[] DEFAULT_PARSE_CLASS = new Class[]{
            BundleParse.class, IntentParse.class, CollectionParse.class,
            MapParse.class, ThrowableParse.class, ReferenceParse.class, MessageParse.class,
            ActivityParse.class
    };
}
