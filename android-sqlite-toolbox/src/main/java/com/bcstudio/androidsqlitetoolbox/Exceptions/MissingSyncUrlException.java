package com.bcstudio.androidsqlitetoolbox.Exceptions;

/**
 * This exception is raised when sync url is empty or null
 */

public final class MissingSyncUrlException extends Exception {
    private static final long serialVersionUID = 1L;

    public MissingSyncUrlException(String msg) {
        super(msg);
    }

    public MissingSyncUrlException(String msg, Throwable cause) {
        super(msg, cause);
    }

    /**
     * Creates exception with the specified cause. Consider using
     * {@link #MissingSyncUrlException(String, Throwable)} instead if you can describe what happened.
     *
     * @param cause root exception that caused this exception to be thrown.
     */
    public MissingSyncUrlException(Throwable cause) {
        super(cause);
    }
}
