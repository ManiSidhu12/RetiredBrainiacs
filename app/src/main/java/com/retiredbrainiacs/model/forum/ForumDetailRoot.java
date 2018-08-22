package com.retiredbrainiacs.model.forum;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ForumDetailRoot {

@SerializedName("status")
@Expose
private String status;
@SerializedName("message")
@Expose
private String message;
@SerializedName("form_main")
@Expose
private List<FormMain> formMain = null;
@SerializedName("proposal_data")
@Expose
private List<Object> proposalData = null;
@SerializedName("form_messages")
@Expose
private List<FormMessage> formMessages = null;

public String getStatus() {
return status;
}

public void setStatus(String status) {
this.status = status;
}

public String getMessage() {
return message;
}

public void setMessage(String message) {
this.message = message;
}

public List<FormMain> getFormMain() {
return formMain;
}

public void setFormMain(List<FormMain> formMain) {
this.formMain = formMain;
}

public List<Object> getProposalData() {
return proposalData;
}

public void setProposalData(List<Object> proposalData) {
this.proposalData = proposalData;
}

public List<FormMessage> getFormMessages() {
return formMessages;
}

public void setFormMessages(List<FormMessage> formMessages) {
this.formMessages = formMessages;
}

}