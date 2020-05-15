package com.tourcoo.training.core.log.cores.parsers;


import com.tourcoo.training.core.log.cores.IParser;

import java.util.ArrayList;
import java.util.List;


/**
 * @author :JenkinsZhou
 * @description :解析器管理者
 * @company :途酷科技
 * @date 2020年03月07日22:16
 * @Email: 971613168@qq.com
 */
public class ParserManager {

    private List<IParser> parseList;
    private volatile static ParserManager singleton;

    private ParserManager() {
        this.parseList = new ArrayList<>();
    }

    public static ParserManager getInstance() {
        if (singleton == null) {
            synchronized (ParserManager.class) {
                if (singleton == null) {
                    singleton = new ParserManager();
                }
            }
        }
        return singleton;
    }

    public synchronized void addParserClass(Class<? extends IParser>... classes) {
        for (Class<? extends IParser> cla : classes) {
            try {
                parseList.add(0, cla.newInstance());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public List<IParser> getParseList() {
        return parseList;
    }
}
