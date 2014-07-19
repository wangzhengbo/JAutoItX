@echo off

set JAutoItX_VERSION=3.3.10.2-1.0.3

set CLASSPATH=.\lib\commons-io-2.2.jar
set CLASSPATH=%CLASSPATH%;.\lib\commons-lang3-3.1.jar
set CLASSPATH=%CLASSPATH%;.\lib\hamcrest-core-1.3.jar
set CLASSPATH=%CLASSPATH%;.\lib\jna-3.5.2.jar
set CLASSPATH=%CLASSPATH%;.\lib\jna-platform-3.5.2.jar
set CLASSPATH=%CLASSPATH%;.\lib\junit-4.11.jar
set CLASSPATH=%CLASSPATH%;.\lib\org.eclipse.jface_3.9.1.v20130725-1141.jar
set CLASSPATH=%CLASSPATH%;.\lib\org.eclipse.jface.databinding_1.6.200.v20130515-1857.jar
set CLASSPATH=%CLASSPATH%;.\lib\org.eclipse.jface.text_3.8.101.v20130802-1147.jar
set CLASSPATH=%CLASSPATH%;.\lib\sigar.jar
set CLASSPATH=%CLASSPATH%;.\dist\%JAutoItX_VERSION%\JAutoItX-%JAutoItX_VERSION%.jar
set CLASSPATH=%CLASSPATH%;.\dist\%JAutoItX_VERSION%\JAutoItX-%JAutoItX_VERSION%-test.jar

IF /i "%PROCESSOR_IDENTIFIER:~0,3%"=="X86" (
	set CLASSPATH=%CLASSPATH%;.\lib\swt-4.4.jar
) ELSE (
	set CLASSPATH=%CLASSPATH%;.\lib\swt-4.4-x64.jar
)

set TESTS=AllTests
IF "%1" NEQ "" (
	set TESTS="%1"
)
java -cp %CLASSPATH% org.junit.runner.JUnitCore cn.com.jautoitx.%TESTS%
pause
