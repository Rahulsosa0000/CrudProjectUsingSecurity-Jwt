package com.crud.filter;

import java.io.IOException;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.crud.config.MyUserDetails;
import com.crud.config.MyUserDetailsService;
import com.crud.entity.Users;
import com.crud.repo.UsersRepo;
import com.crud.services.JwtService;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class Jwtfilter extends OncePerRequestFilter {

    private Logger logger = LoggerFactory.getLogger(OncePerRequestFilter.class);
    @Autowired
    private JwtService jwtHelper;

    @Autowired
    private MyUserDetailsService userDetailsService;
    
    @Autowired
    private UsersRepo repo;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        String requestHeader = request.getHeader("Authorization");
        //Bearer 2352345235sdfrsfgsdfsdf send token in header
        logger.info(" Header :  {}", requestHeader);
        String username = null;
       
        String token = null;
       

        if (requestHeader != null && requestHeader.startsWith("Bearer")) {  // if header is null throw NullPointer exception 
            //looking good
            token = requestHeader.substring(7);
            logger.info(" token :  {}", token);
            try {
            	

                username = jwtHelper.extractUserName(token); // give username from the token
                logger.info(" username :  {}", username);
            } catch (IllegalArgumentException e) {
                logger.info("Illegal Argument while fetching the username !!");
                e.printStackTrace();
            } catch (ExpiredJwtException e) {
                logger.info("Given jwt token is expired !!");
                // Extract the username from the expired token to generate a new token
                username = e.getClaims().getSubject();
                logger.info(" username :  {}", username);

                // Generate new token after extracting the username
                String newToken = jwtHelper.againGenerateToken(username);  // Now uses your new method
                logger.info(" newToken :  {}", newToken);

                // Update the token in the database
                Users user = repo.findByusername(username);  // Fetch the user from the database using the username
 
                if (user != null)
                {
                    user.setToken(newToken);  // Assuming you have a 'token' field in the Usertable model
                    repo.save(user);  // Save the updated user to the database
                }
                else
                {
                    logger.error("User not found in the database.");
                }
                
                // Set the new token in the response header
                response.setHeader("Authorization", "Bearer " +newToken);
                
                // Return the response with a status and message
                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"Token expired, new token generated! \n\"New Token\": \"" + newToken + "\"}");
                return;
                
            } catch (MalformedJwtException e) {
                logger.info("Some changed has done in token !! Invalid Token");
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();

            }


        } else {
            logger.info("Invalid Header Value !! ");
        }
  
        // if username not null and context is null so set authentication
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {


            //fetch user detail from username
            MyUserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            Boolean validateToken = this.jwtHelper.validateToken(token, userDetails);//to tell the helper check user or token
            logger.info(" validateToken :  {}", validateToken);
            if (validateToken) {

                //set the authentication
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
               // authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); // WebAuthenticationDetailsSource contain IP address from which the HTTP request or session id
                SecurityContextHolder.getContext().setAuthentication(authentication);

               // logger.info(" authentication :  {}", authentication); // information about SetDetails request

            } else {
                logger.info("Validation fails !	!");
            }


        }

        filterChain.doFilter(request, response);


    }
    
    
    
}