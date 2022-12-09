package com.example.springsecuritydemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author ycy
 * 解决跨域请求
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        // Response Headers里面的Access-Control-Allow-Origin: http://localhost:8080
        // 允许所有跨域：config.addAllowedOrigin("*");
        config.addAllowedOrigin("http://localhost:8080");

        // 设置是否发送cookie信息，在前端也可以设置axios.defaults.withCredentials = true;表示发送Cookie,
        // 跨域请求要想带上cookie，必须要设置请求属性withCredentials=true，这是浏览器的同源策略导致的问题：不允许JS访问跨域的Cookie
        /**
         * withCredentials前后端都要设置，后端是setAllowCredentials来设置
         * 如果后端设置为false而前端设置为true，前端带cookie就会报错
         * 如果后端为true，前端为false，那么后端拿不到前端的cookie，cookie数组为null
         * 前后端都设置withCredentials为true，表示允许前端传递cookie到后端。
         * 前后端都为false，前端不会传递cookie到服务端，后端也不接受cookie
         */
        // Response Headers里面的Access-Control-Allow-Credentials: true
        config.setAllowCredentials(false);

        // Response Headers里面的Access-Control-Allow-Methods属性
        // 设置允许请求的方式，比如get、post、put、delete，*表示全部
        config.addAllowedMethod("*");

        // Response Headers里面的Access-Control-Allow-Headers属性
        // 设置允许访问的请求头，比如content-type, headeruserid, headerusertoken，*表示全部
        config.addAllowedHeader("*");

        // Response Headers里面的Access-Control-Max-Age属性
        // 设置当前跨域请求最大有效时长
        // 以Access-Control-Max-Age:3600为例
        // 表示下回同一个接口post请求，在3600s之内不会发送options请求，
        // 不管post请求成功还是失败，3600s之内不会再发送options请求
        // 如果不设置这个，那么 每次post请求 前 必有options请求
        // 设置为1天
        long maxAge = 24 * 60 * 60;
        config.setMaxAge(maxAge);

        config.addExposedHeader("Authorization");

        // 2. 为url添加映射路径
        UrlBasedCorsConfigurationSource corsSource = new UrlBasedCorsConfigurationSource();
        // /**表示该config适用于所有路由
        corsSource.registerCorsConfiguration("/**", config);

        // 3. 返回重新定义好的corsSource
        return new CorsFilter(corsSource);
    }

}
