package com.wms.config;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * This handler catches all exceptions that are not specifically handled by other methods.
     * It logs the error and redirects the user to our custom error page.
     *
     * @param e The exception that was thrown.
     * @return A ModelAndView object that directs the user to the 'error.html' page.
     */
    @ExceptionHandler(Exception.class)
    public ModelAndView handleAllExceptions(Exception e) {
        // Log the exception for debugging purposes
        logger.error("An unexpected error occurred: {}", e.getMessage(), e);

        // Prepare the model and view for the error page
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("error"); // Set the view to error.html
        modelAndView.addObject("errorMessage", "An unexpected internal error occurred. Please contact support.");
        
        return modelAndView;
    }
    
    /**
     * A more specific handler for when an entity (like an Item or Order) is not found.
     */
    @ExceptionHandler({IllegalArgumentException.class, EntityNotFoundException.class})
    public ModelAndView handleNotFoundExceptions(Exception e) {
        logger.error("A data access error occurred: {}", e.getMessage());

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("error");
        modelAndView.addObject("errorMessage", e.getMessage()); // Pass the specific error message to the view
        
        return modelAndView;
    }
}