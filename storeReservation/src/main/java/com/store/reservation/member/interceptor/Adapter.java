//package com.store.reservation.member.interceptor;
//
//import org.springframework.core.MethodParameter;
//import org.springframework.http.HttpInputMessage;
//import org.springframework.http.converter.HttpMessageConverter;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;
//
//import java.lang.reflect.Type;
//@RestControllerAdvice
//public class Adapter extends RequestBodyAdviceAdapter {
//
//    @Override
//    public Object afterBodyRead(final Object body, final HttpInputMessage inputMessage,
//                                final MethodParameter parameter,
//                                final Type targetType, final Class<? extends HttpMessageConverter<?>> converterType) {
//
//        System.out.printf("afterBodyRead ");
//        if (body instanceof ReqBody) {
//            ReqBody reqBody = (ReqBody) body;
//            System.out.printf("dong = %s, ho = %s\n", reqBody.getDong(), reqBody.getHo());
//        }
//
//        return body;
//    }
//
//    @Override
//    public HttpInputMessage beforeBodyRead(final HttpInputMessage inputMessage,
//                                           final MethodParameter parameter,
//                                           final Type targetType, final Class<? extends HttpMessageConverter<?>> converterType)
//            throws IOException {
//        System.out.println("beforeBodyRead");
//        return inputMessage;
//    }
//
//    @Override
//    public Object handleEmptyBody(final Object body, final HttpInputMessage inputMessage,
//                                  final MethodParameter parameter, final Type targetType,
//                                  final Class<? extends HttpMessageConverter<?>> converterType) {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    @Override
//    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
//        return false;
//    }
//}
