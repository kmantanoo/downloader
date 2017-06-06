package model.datasource;

import config.AppConfig;

public class DataSourceFactory {

   public static DataSource newInstance(String dataSourceClassPath, String source)
         throws InstantiationException, IllegalAccessException, ClassNotFoundException {
      
      DataSource dtSource = (DataSource) Class.forName(dataSourceClassPath).newInstance();
      dtSource.setSource(source);
      dtSource.setAppConfig(AppConfig.getInstance());
      return dtSource;
   }
}
