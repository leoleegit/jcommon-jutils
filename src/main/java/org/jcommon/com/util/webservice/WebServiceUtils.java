// ========================================================================
// Copyright 2012 leolee<workspaceleo@gmail.com>
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//     http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
// ========================================================================
package org.jcommon.com.util.webservice;
import java.net.MalformedURLException;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.description.OperationDesc;
import org.apache.axis.description.ParameterDesc;
import javax.xml.namespace.*;
import javax.xml.rpc.ServiceException;

public class WebServiceUtils {
	private static String endpoint  = null;
	private static String namespace = null;
	
	public static void init(String endpoint, String namespace){
		WebServiceUtils.endpoint = endpoint;
		WebServiceUtils.namespace= namespace;
	}
	
	@SuppressWarnings("rawtypes")
	public static ParameterDesc getParameter(String name, Class class_){
		if(!isReady()) return null;
		
		ParameterDesc param = new org.apache.axis.description.ParameterDesc(
        		new QName(namespace, name),
        		ParameterDesc.IN, 
        		new QName("http://www.w3.org/2001/XMLSchema", class_.getSimpleName().toLowerCase()), 
        		java.lang.String.class, false, false);
		param.setOmittable(true);
		return param;
	}
	
	@SuppressWarnings("rawtypes")
	public static OperationDesc getOperationDesc(String name, Class return_class){
		if(!isReady()) return null;
	    
		OperationDesc oper = new org.apache.axis.description.OperationDesc();
        oper.setName(name);
        oper.setReturnType(new QName("http://www.w3.org/2001/XMLSchema", return_class.getSimpleName().toLowerCase()));
        oper.setReturnClass(return_class);
        oper.setReturnQName(new QName(namespace, "return"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        return oper;
	}
	
	public static Call getCall(String endpoint, String namespace) throws ServiceException, MalformedURLException{
		init(endpoint,namespace);
		Service service = new Service();
		Call call = null;
		call = (Call) service.createCall();
        call.setUseSOAPAction(true);
        call.setSOAPActionURI("");
        call.setEncodingStyle(null);
        call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        call.setTargetEndpointAddress(new java.net.URL(endpoint));
        return call;
	}
	
	private static boolean isReady(){
		return endpoint!=null && namespace!=null;
	}
}
