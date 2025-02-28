package com.xyt.init.business.domain.service;

import java.util.function.Consumer;

public interface AiService {


    Object chat(String question, Consumer<String> onMessage);
}
