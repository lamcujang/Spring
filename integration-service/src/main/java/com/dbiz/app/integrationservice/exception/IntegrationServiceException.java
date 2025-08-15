package com.dbiz.app.integrationservice.exception;

public class IntegrationServiceException extends RuntimeException{

        private static final long serialVersionUID = 1L;

        public IntegrationServiceException(String message) {
            super(message);
        }

        public IntegrationServiceException(String message, Throwable cause) {
            super(message, cause);
        }
}
