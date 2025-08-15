//package com.dbiz.app.proxyclient.business.cache;
//
//import com.dbiz.app.proxyclient.business.user.service.CacheService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/cache")
//@RequiredArgsConstructor
//public class CacheController {
//
//    private final CacheService cacheService;
//
//    @GetMapping("/find")
//    public Object find(@RequestParam String key) {
//        return cacheService.getCacheValue(key);
//    }
//
//    @DeleteMapping("/clear")
//    public void clearAllCaches() {
//        cacheService.clearAllCaches();
//    }
//
//
//    @PostMapping("/save")
//    public void save(@RequestParam String key, @RequestParam String value) {
//        cacheService.setCacheValue(key, value);
//    }
//    @DeleteMapping("/delete")
//    public void delete(String key) {
//        cacheService.deleteCacheValue(key);
//    }
//
//    @GetMapping("/vendor")
//    public Object getVendorFromCache(@RequestParam String value,@RequestParam String id) {
//        return cacheService.getVendorFromCache(value,id);
//    }
//
//    @GetMapping("/ping")
//    public String ping() {
//        return cacheService.ping();
//    }
//}