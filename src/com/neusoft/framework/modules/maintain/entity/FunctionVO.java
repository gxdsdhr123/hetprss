package com.neusoft.framework.modules.maintain.entity;

import java.io.Serializable;

public class FunctionVO  implements Serializable{
	private static final long serialVersionUID = -2475084233664923686L;
	private String functionid;
	private String maintainId;
	private String functionName;
	private String functionBody;
	private String functionPosition;
	private String iconName;
	private String functionParam;
	private String functionDesc;
	public String getFunctionid() {
		return functionid;
	}
	public void setFunctionid(String functionid) {
		this.functionid = functionid;
	}
	public String getMaintainId() {
		return maintainId;
	}
	public void setMaintainId(String maintainId) {
		this.maintainId = maintainId;
	}
	public String getFunctionName() {
		return functionName;
	}
	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}
	public String getFunctionBody() {
		return functionBody;
	}
	public void setFunctionBody(String functionBody) {
		this.functionBody = functionBody;
	}
	public String getFunctionPosition() {
		return functionPosition;
	}
	public void setFunctionPosition(String functionPosition) {
		this.functionPosition = functionPosition;
	}
	public String getIconName() {
		return iconName;
	}
	public void setIconName(String iconName) {
		this.iconName = iconName;
	}
	public String getFunctionParam() {
		return functionParam;
	}
	public void setFunctionParam(String functionParam) {
		this.functionParam = functionParam;
	}
	public String getFunctionDesc() {
		return functionDesc;
	}
	public void setFunctionDesc(String functionDesc) {
		this.functionDesc = functionDesc;
	}
	
	public String getFunctionString(){
		StringBuffer functionString=new StringBuffer();
		functionString.append(" function "+this.functionName+"() {");
		functionString.append(" var param=''; ");
		functionString.append(" if(arguments){ for(var i=0;i<arguments.length;i++){ ");
		functionString.append(" param=param+'&'+arguments[i] ;");
		functionString.append("} }");
		functionString.append( this.functionBody );
		functionString.append("  }");
		return functionString.toString();
	}
	
}
