package affableBean.validation;

import java.util.Collection;
import java.util.HashMap;

public class ValidationResponse {
	 private String status;
	 private HashMap errorMessageList;

	 public String getStatus() {
	   return status;
	 }
	 public void setStatus(String status) {
	   this.status = status;
	 }
	 public HashMap getErrorMessageList() {
	   return this.errorMessageList;
	 }
	 public void setErrorMessageList(HashMap errorMessageList) {
	   this.errorMessageList = errorMessageList;
	 }
	}