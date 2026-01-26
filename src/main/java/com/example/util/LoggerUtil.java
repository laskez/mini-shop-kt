package com.example.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerUtil {

    // Метод для получения логгера по классу
    public static Logger getLogger(Class<?> clazz) {
        return LoggerFactory.getLogger(clazz);
    }

    // Метод для логирования начала HTTP запроса
    public static void logRequestStart(Logger logger, String method, String uri, String remoteAddr) {
        logger.info("▶ {} {} от IP: {}", method, uri, remoteAddr);
    }

    // Метод для логирования окончания HTTP запроса
    public static void logRequestEnd(Logger logger, String method, String uri, long durationMs) {
        logger.info("◀ {} {} завершен за {} мс", method, uri, durationMs);
    }

    // Метод для логирования ошибок
    public static void logError(Logger logger, String message, Throwable throwable) {
        logger.error("❌ {}: {}", message, throwable.getMessage(), throwable);
    }

    // Метод для логирования бизнес-событий
    public static void logBusinessEvent(Logger logger, String event, Object... params) {
        logger.info("📊 {} {}", event, params);
    }
}