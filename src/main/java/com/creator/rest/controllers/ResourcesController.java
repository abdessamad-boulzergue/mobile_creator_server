package com.creator.rest.controllers;


import com.creator.models.Resource;
import com.creator.models.ResourceType;
import com.creator.services.ResourcesService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

@RestController
@RequestMapping("creator/api/v1/resource")
@CrossOrigin
public class ResourcesController {

    @Autowired
    ResourcesService resourceService;

    @GetMapping("/type/{id}")
    public ResourceType getType(@PathVariable("id") Long id) {
       return  resourceService.getType(id);
    }

    @PostMapping("/{type}/new")
    public Resource createDocument(@PathVariable("type") String resourceType) {

        Resource resource =  Resource.getResource(resourceType);
        Resource savedResource = resourceService.saveResource(resource );
        return  savedResource;
    }
    @GetMapping("/{id}")
    public ResponseEntity<String> load(@PathVariable(name="id") String id){
        String resource = resourceService.readResourceFromFile(id);
        return ResponseEntity.status(HttpStatus.OK).body(resource );
    }

    @GetMapping("/types")
    public List<ResourceType> getTypes() {
        return resourceService.getTypes();
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getResources() {

        try {
            List<Resource> resources = resourceService.getResources();
            return ResponseEntity.status(HttpStatus.OK).body(resources);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<Object> deleteResource(@PathVariable("id") Long id) {

        resourceService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body("resource deleted");

    }
    @PostMapping("/document")
    public ResponseEntity<String> saveDocument(@RequestBody String jsonString){
        try {
            ResourceType type = resourceService.getType(ResourceType.TYPE_DOCUMENT);
            Resource savedResource = saveResource(jsonString,type );
            if(savedResource==null && savedResource.getId()<=0) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("save failed") ;
            }
        } catch (Exception e) {
            String msg = (e!=null)? e.getMessage() : "";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(msg) ;
        }
        return ResponseEntity.status(HttpStatus.OK).body("ok") ;
    }
    @PostMapping("/application")
    public ResponseEntity<String> saveApplication(@RequestBody String jsonString){
        try {
            ResourceType type = resourceService.getType(ResourceType.TYPE_APPLICATION);
            Resource savedResource = saveResource(jsonString,type );
            if(savedResource==null && savedResource.getId()<=0) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("save failed") ;
            }
        } catch (Exception e) {
            String msg = (e!=null)? e.getMessage() : "";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(msg) ;
        }
        return ResponseEntity.status(HttpStatus.OK).body("ok") ;
    }
    private Resource saveResource(String jsonString, ResourceType type ) throws UnsupportedEncodingException {

            String content = URLDecoder.decode(jsonString,"utf-8");
            JSONArray json = new JSONArray(content);
            JSONObject attributes = json.getJSONObject(1);
            Resource resource = Resource.from(attributes);
            resource.setType(type);
            Resource savedResource = resourceService.saveResource(resource);
            if(savedResource!=null && savedResource.getId()>0) {
                String versionName = savedResource.getMaxVersion().getName();
                attributes.put("version", versionName);
                resourceService.writeResourceTofile(String.valueOf(savedResource.getId()), json.toString());
                return savedResource;

            }else {
                return null ;
            }
    }
    @GetMapping
    public ResponseEntity<Object> getResource(@RequestParam("id") Long id) {

        try {
            Resource resource = resourceService.get(id);
            return ResponseEntity.status(HttpStatus.OK).body(resource);

        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


}
