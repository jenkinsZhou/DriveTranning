package com.tourcoo.training.core.log.cores.parsers;

import android.os.Message;

import androidx.annotation.NonNull;

import com.tourcoo.training.core.log.cores.IParser;
import com.tourcoo.training.core.log.cores.utils.ObjectUtil;


class MessageParse implements IParser<Message> {
    @NonNull
    @Override
    public Class<Message> parseClassType() {
        return Message.class;
    }

    @Override
    public String parseString(@NonNull Message message) {
        return message.getClass().getName() + " [" + LINE_SEPARATOR +
                String.format("%s = %s", "what", message.what) + LINE_SEPARATOR +
                String.format("%s = %s", "when", message.getWhen()) + LINE_SEPARATOR +
                String.format("%s = %s", "arg1", message.arg1) + LINE_SEPARATOR +
                String.format("%s = %s", "arg2", message.arg2) + LINE_SEPARATOR +
                String.format("%s = %s", "data", new BundleParse().parseString(message.getData())) + LINE_SEPARATOR +
                String.format("%s = %s", "obj", ObjectUtil.objectToString(message.obj)) + LINE_SEPARATOR +
                "]";
    }
}
