//package org.example.vertx.config;
//
//import io.vertx.core.Vertx;
//import lombok.extern.slf4j.Slf4j;
//import org.example.vertx.verticle.factory.SpringVerticleFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.FilterType;
//import org.springframework.core.type.ClassMetadata;
//import org.springframework.core.type.classreading.MetadataReader;
//import org.springframework.core.type.classreading.MetadataReaderFactory;
//import org.springframework.core.type.filter.TypeFilter;
//
//import java.io.IOException;
//
///**
// * VerticleFilter
// *
// * @Author: taomee
// * @Date: 2020/4/11 0011 17:33
// * @Description:
// */
//@Slf4j
//@Configuration
//@ComponentScan(basePackages = "org.example.vertx",
//        includeFilters =
//        @ComponentScan.Filter(
//                type = FilterType.CUSTOM,
//                classes = VerticleFilter.class),
//        useDefaultFilters = false)
//public class VerticleFilter implements TypeFilter {
//    @Autowired
//    SpringVerticleFactory factory;
//
//    @Override
//    public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
//        //获取当前正在扫描的类的类信息
//        ClassMetadata classMetadata = metadataReader.getClassMetadata();
//        log.info("VerticleFilter test class:{}", classMetadata.getClassName());
//       String superClass = classMetadata.getSuperClassName();
//        if(superClass.equalsIgnoreCase("io.vertx.core.AbstractVerticle")){
//            Vertx vertx = Vertx.vertx();
//            vertx.registerVerticleFactory(factory);
//            vertx.deployVerticle(factory.prefix() + classMetadata.getClassName());
//        }
//        return true;
//    }
//}
