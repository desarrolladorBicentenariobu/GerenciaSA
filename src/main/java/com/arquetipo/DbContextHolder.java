/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arquetipo;

/**
 *
 * @author roberto.espinoza
 */

public class DbContextHolder {

   private static final ThreadLocal<DbType> contextHolder = new ThreadLocal<DbType>();

   public static void setDbType(DbType dbType) {
       if(dbType == null){
           throw new NullPointerException();
       }
      contextHolder.set(dbType);
   }

   public static DbType getDbType() {
      return (DbType) contextHolder.get();
   }

   public static void clearDbType() {
      contextHolder.remove();
   }
}
