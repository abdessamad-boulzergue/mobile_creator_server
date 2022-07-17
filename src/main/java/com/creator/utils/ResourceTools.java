package com.creator.utils;

import com.creator.models.Resource;
import org.json.JSONArray;
import org.json.JSONObject;

public class ResourceTools {

	
	public static final int INDEX_TYPE = 0;
	public static final int INDEX_ATTRIBUTES = 1;
	public static final int INDEX_CHILDREN = 2;

	public static final String CONTENT = "CONTENT";
	public static final  String WF_REPOSITORY_WORKFLOW_TYPE  = "repository:Workflow";
	public static final String WF_Description_TYPE = "xpdl:Description";
	public static final String  WF_ExtendedAttributes_TYPE = "xpdl:ExtendedAttributes";
	public static final String WF_Implementation_TYPE = "xpdl:Implementation";
	public static final String WF_Tool_TYPE = "xpdl:Tool";
	public static final String WF_ACTIVITIES_TYPE = "xpdl:Activities";
	public static final  String WF_ACTIVITY_TYPE = "xpdl:Activity";
	public static final String WF_TRANSITIONS_TYPE = "xpdl:Transitions";
	public static final String WF_TRANSITION_TYPE = "xpdl:Transition";
	public static final String WF_PLUGIN_PYTHON_TYPE = "pluginPython";
    public static final String WF_XPDL_TYPE = "XPDL";
    public static final String WF_XPDL_PACKAGE_TYPE = "xpdl:Package";
    public static final String WF_XPDL_WORKFLOW_PROCESSES_TYPE = "xpdl:WorkflowProcesses";
    public static final String WF_XPDL_WORKFLOW_PROCESS_TYPE = "xpdl:WorkflowProcess";
	protected static final String pointX_START = "250";
	protected static final String pointY_START = "50";
	
	protected static final String pointX_TRANSITION = "250";
	protected static final String pointY_TRANSITION = "150";
	
	protected static final String pointX_END = "300";
	protected static final String pointY_END = "250";
	protected static final String START_ACTIVITY_NAME = "start";
	public static final String ATTR_NAME = "name";
    public static final String ATTR_RESDESC_ID = "resourceId";
    public static final String ATTR_ID = "id";
	public static final String ATTR_TYPE = "type";
	public static final String ATTR_VERSION = "version";
    public static final String POINT_X = "pointX";
    public static final String POINT_Y = "pointY";
    
	public static final String WF_REPOSITORY_WORKFLOW_JOB_CONFIG  = "repository:WorkflowJobConfiguration";
    

	public static JSONArray getContent(JSONArray basicElement) {
		return getChildNodeOfType(basicElement, CONTENT);
	}

	public static JSONArray getDefaultImplementation(JSONObject extendedAttributes, JSONObject attributes){
		JSONArray implementationNode = createBasicElement(WF_Implementation_TYPE);
		JSONArray toolNode = getDefaultXpdlTool(extendedAttributes,attributes);
        addChildren(implementationNode,toolNode);

        return implementationNode;
      }
	
	public static JSONArray getDefaultXpdlTool(JSONObject extendedAttributes,JSONObject attributes){
		  JSONArray toolNode = createBasicElement(WF_Tool_TYPE);
          setAttributes(toolNode,attributes);
          JSONArray extendAttrsToolNode = createBasicElement(WF_ExtendedAttributes_TYPE);
          setAttributes(extendAttrsToolNode,extendedAttributes);
          addChildren(toolNode, extendAttrsToolNode);
          return toolNode;
        }
	
	public static JSONArray getChildNodeOfType(JSONArray basicElement, String typeString) {
		
		if(typeString.equals(getResourceType(basicElement))) {
			return basicElement;
		}
        JSONArray contentArray = getChildren(basicElement);
        JSONArray nodeOfType = null;
        for(Object child : contentArray){
        	if(child instanceof JSONArray && typeString.equals(getResourceType((JSONArray)child)) ) {
        		nodeOfType = (JSONArray) child;
        		return nodeOfType;
        	}else if(child instanceof JSONArray) {
        		
				for(Object litleChilde : getChildren((JSONArray)child)) {
					JSONArray found = getChildNodeOfType((JSONArray)litleChilde ,typeString);
				 if(found!=null)
					return found;
        	}
        	}
        }
 
        return nodeOfType;
	}

	public static String getAttribute (JSONArray resource, String attributeName) {
		if (!isBasicElement(resource)) {
            throw new IllegalArgumentException("resource is not a basic element [_name_ , {_attributes_} , [_content_] ]") ;
        }
		
        if( resource.getJSONObject(INDEX_ATTRIBUTES).has(attributeName)) {
        	return resource.getJSONObject(INDEX_ATTRIBUTES).getString(attributeName);
        }
        else {
        	return "";
        }
    }

	public static String getResourceType(JSONArray resource) {
		if (!isBasicElement(resource)) {
            throw new IllegalArgumentException("resource is not a basic element [_name_ , {_attributes_} , [_content_] ] : "+resource.toString()) ;
        }
		return resource.getString(INDEX_TYPE);
	}


	public static JSONArray  getChildren(JSONArray resource) {
		if (!isBasicElement(resource)) {
            throw new IllegalArgumentException("resource is not a basic element [_name_ , {_attributes_} , [_content_] ]") ;
        }
		return (JSONArray) resource.get(INDEX_CHILDREN);
	}


	public static void addChildren(JSONArray resource, JSONArray children) {
		
		if (!isBasicElement(resource)) {
            throw new IllegalArgumentException("resource is not a basic element [_name_ , {_attributes_} , [_content_] ]") ;
        }
		
		((JSONArray)resource.get(INDEX_CHILDREN)).put(children);
		
	}

	public static void setAttributes(JSONArray resource, JSONObject attrs) {
		if (!isBasicElement(resource)) {
            throw new IllegalArgumentException("resource is not a basic element [_name_ , {_attributes_} , [_content_] ]") ;
        }
		resource.put(INDEX_ATTRIBUTES,attrs);
	}

	public static boolean isBasicElement(JSONArray resource) {
		return 
                resource.length() == 3
                && resource.get(INDEX_TYPE) instanceof String
                && resource.get(INDEX_ATTRIBUTES) instanceof JSONObject
                && resource.get(INDEX_CHILDREN) instanceof JSONArray;
		
	}

	public static JSONArray createBasicElement(String beType) {
		return createBasicElement(beType,false);
	}
		public static JSONArray createBasicElement(String beType, boolean withContent) {
		JSONArray content = new JSONArray();
		JSONArray basicElement = new JSONArray();
		basicElement.put(beType);
		
		if(withContent) {
			content.put( createBasicElement(CONTENT , false));
		}
		basicElement.put(new JSONObject());
		basicElement.put(content);
		
        return basicElement;
	}
		
		public static JSONObject getAttributes(JSONArray resource) {
			if (!isBasicElement(resource)) {
				throw new IllegalArgumentException("resource is not a basic element [_name_ , {_attributes_} , [_content_] ]") ;
			}
			return (JSONObject) resource.get(INDEX_ATTRIBUTES);
		}

		public static JSONArray toJsonArray(Resource resource) {
		  return toJsonArray(resource.getType().getName(), resource.toJson());
		}
		public static JSONArray toJsonArray(String type, JSONObject attributes) {

			    	JSONArray resourceNode = createBasicElement(type,true);
			        setAttributes(resourceNode,attributes);
			        return resourceNode;
		}
	
}
