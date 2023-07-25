# Ref 
Developed based on [helm-maven-plugin](https://github.com/deweya/helm-maven-plugin)

# ocp-maven-plugin
A Maven plugin for deploying my-camel applications to OpenShift platforms.

# maven goals

## kustomize
* ocp:apply - Create/Upgrade a my-camel application
* ocp:delete - Uninstall an existing my-camel application
* ocp:kustomize - Generate merged deployment template for preview
* ocp:pull-base - Pull kustomize base in src/ocp/kustomize directory

## helm
* ocp:helm-upgrade - Create/Upgrade a my-camel application
* ocp:helm-delete - Uninstall an existing my-camel application

# Sample
## kustomize apply
    ```xml
    <profile>
        <!-- Use this profile to generate Java API model for application runtime -->
        <!-- mvn ocp:apply -Ddeploy=true -DKUSTOMIZE_OVERLAY=sandbox -->
        <!-- mvn ocp:kustomize -Ddeploy=true -DKUSTOMIZE_OVERLAY=sandbox -->
        <!-- mvn ocp:pull-base -Ddeploy=true -->
        <id>ocp-deploy</id>
        <activation>
            <property>
                <name>deploy</name>
                <value>true</value>
            </property>
        </activation>
        <build>
            <plugins>
                <!-- 
                From local build of
                    https://XiaochunFrankLi@dev.azure.com/XiaochunFrankLi/portal-poc/_git/ocp-maven-plugin
                -->
                <plugin>
                    <groupId>ocp-maven-plugin</groupId>
                    <artifactId>ocp-maven-plugin</artifactId>
                    <version>0.0.1-SNAPSHOT</version>
                    <configuration>
                        <overlay>${KUSTOMIZE_OVERLAY}</overlay>
                    </configuration>
                </plugin>
            </plugins>
        </build>
    </profile>
    ```
## help upgrade
    ```xml
     <plugin>
         <groupId>ocp.deployment</groupId>
         <artifactId>ocp-maven-plugin</artifactId>
         <version>0.0.1</version>
         <configuration>
             <releaseName>my-camel-integrator</releaseName>
             <chart>
                 <repository>
                      <url>build-in://my-camel</url>
                 </repository>
             </chart>
             <namespace>my-camel</namespace>
         </configuration>
         <executions>
            <execution>
                <goals>
                    <goal>upgrade</goal>
                </goals>
            </execution>
         </executions>
     </plugin>
    ```