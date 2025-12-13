package com.example.demo.controllers;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api")
public class RootController {

    @GetMapping
    public RepresentationModel<?> getRoot(){
        RepresentationModel<?> rootModel = new RepresentationModel<>();
        rootModel.add(
                linkTo(methodOn(FlowerController.class).getAllFlowers()).withRel("flowers"),
                linkTo(methodOn(BouquetController.class).getAllBouquets()).withRel("bouquets"),
                linkTo(methodOn(OrderController.class).getAllOrders()).withRel("orders"),
                linkTo(methodOn(OrderController.class).getAllOrders(0, 10)).withRel("orders-paged"),
                linkTo(methodOn(RootController.class).getApiDocumentation()).withRel("documentation")
        );
        return rootModel;
    }

    @GetMapping("/docs")
    public RepresentationModel<?> getApiDocumentation() {
        RepresentationModel<?> docsModel = new RepresentationModel<>();
        docsModel.add(
                linkTo(methodOn(RootController.class).getRoot()).withRel("api-root"),
                linkTo(getClass()).slash("swagger-ui.html").withRel("swagger-ui").withType("GET"),
                linkTo(getClass()).slash("v3/api-docs").withRel("openapi-spec").withType("GET")
        );
        return docsModel;
    }
}
