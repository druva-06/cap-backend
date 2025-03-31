package com.consultancy.education.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractLogger {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
}

