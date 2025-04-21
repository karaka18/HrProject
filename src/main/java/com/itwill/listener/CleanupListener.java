package com.itwill.listener;

import com.mysql.cj.jdbc.AbandonedConnectionCleanupThread;

public class CleanupListener implements javax.servlet.ServletContextListener {
    @Override
    public void contextDestroyed(javax.servlet.ServletContextEvent sce) {
        try {
            AbandonedConnectionCleanupThread.checkedShutdown(); // 예외 처리 블록 제거
        } catch (Exception e) {
            e.printStackTrace();  // 예외 처리 필요하면 적절한 로깅 처리
        }
    }

    @Override
    public void contextInitialized(javax.servlet.ServletContextEvent sce) {
        // Nothing to do
    }
}
