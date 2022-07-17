package com.creator.rest.controllers;


import com.creator.models.Resource;
import com.creator.models.ResourceType;
import com.creator.services.ResourcesService;
import com.creator.utils.ResourceTools;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
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

    @GetMapping
    public ResponseEntity<Object> getResource(@RequestParam("id") Long id) {

        try {
            Resource resource = resourceService.get(id);
            return ResponseEntity.status(HttpStatus.OK).body(resource);

        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<Object> saveResource(@Valid @RequestBody Resource resource) {

        Resource savedResource = resourceService.saveResource(resource);
        return ResponseEntity.status(HttpStatus.OK).body(savedResource);


    }

}
