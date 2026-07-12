package com.example.ServiceA.ServiceA.Exception;

public class DuplicateCustomerPANException extends RuntimeException {
  public DuplicateCustomerPANException(String message) {
    super(message);
  }

    public static class InvalidIdentityProofException extends RuntimeException {
      public InvalidIdentityProofException(String message) {
        super(message);
      }
    }

  public static class InvalidIdentityNumberPatternException extends RuntimeException {
      public InvalidIdentityNumberPatternException(String message) {
          super(message);
      }
  }
}
