package com.interface21;

import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.ManualHandlerMapping;

/*어노테이션핸들러랑 매뉴얼헨들러 처리*/
public class HandlerMappingRegistry {
    private final Object[] basePackege;
    public HandlerMappingRegistry(Object... basePackege){
        this.basePackege = basePackege;
    }
    public HandlerMapping[] initialize(){
        HandlerMapping[] handlerMappings = new HandlerMapping[2];
        AnnotationHandlerMapping annotationHandlerMapping=new AnnotationHandlerMapping(basePackege);
        annotationHandlerMapping.initialize();
        handlerMappings[0]=annotationHandlerMapping;
        ManualHandlerMapping manualHandlerMapping=new ManualHandlerMapping(basePackege);
        manualHandlerMapping.initialize();
        handlerMappings[1]=manualHandlerMapping;
        return handlerMappings;
    }
}
