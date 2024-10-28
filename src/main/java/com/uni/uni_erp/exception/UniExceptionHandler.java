package com.uni.uni_erp.exception;

import com.uni.uni_erp.exception.errors.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

// RuntimeException이 발생하면 해당 파일로 오류가 모인다
@ControllerAdvice // C.A --> 뷰 에러 페이지, R.C.A 데이터 반환 (에러)
public class UniExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(UniExceptionHandler.class);

    @ExceptionHandler(Exception400.class)
    public ModelAndView handleException400(Exception400 ex, Model model) {
        ModelAndView mav = new ModelAndView("/err/400");
        mav.addObject("message", ex.getMessage());
        logger.error("Exception400 발생: {}", ex.getMessage(), ex);
        return mav;
    }
    @ExceptionHandler(Exception401.class)
    public ModelAndView handleException401(Exception401 ex, Model model) {
        ModelAndView mav = new ModelAndView("/err/401");
        mav.addObject("message", ex.getMessage());
        logger.error("Exception401 발생: {}", ex.getMessage(), ex);
        return mav;
    }
    @ExceptionHandler(Exception403.class)
    public ModelAndView handleException403(Exception403 ex, Model model) {
        ModelAndView mav = new ModelAndView("/err/403");
        mav.addObject("message", ex.getMessage());
        logger.error("Exception403 발생: {}", ex.getMessage(), ex);
        return mav;
    }

    @ExceptionHandler(Exception404.class)
    public ModelAndView handleException404(Exception404 ex, Model model) {
        ModelAndView mav = new ModelAndView("/err/404");
        mav.addObject("message", ex.getMessage());
        logger.error("Exception404 발생: {}", ex.getMessage(), ex);
        return mav;
    }
    @ExceptionHandler(Exception500.class)
    public ModelAndView handleException500(Exception500 ex, Model model) {
        ModelAndView mav = new ModelAndView("/err/500");
        mav.addObject("message", ex.getMessage());
        logger.error("Exception500 발생: {}", ex.getMessage(), ex);
        return mav;
    }

}
