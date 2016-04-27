// ========================================================================
// Copyright 2012 leolee<workspaceleo@gmail.com>
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//     http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
// ========================================================================
package org.jcommon.com.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
	public static String getNowSinceHour(){
		Date now=new Date();
		return getNowSinceHour(now);
	}
	
	public static String getNowSinceYear(){
		Date now=new Date();
		return getNowSinceYear(now);
	}
	
	public static String getNowSinceYear(Date now){
		SimpleDateFormat f=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return f.format(now);
	}
	
	public static String getNowSinceHour(Date now){
		SimpleDateFormat f=new SimpleDateFormat("HH:mm:ss");
		return f.format(now);
	}
	
	public static Date getDate(String date, String format) throws ParseException{
		if(date==null)
			return null;
		SimpleDateFormat f=new SimpleDateFormat(format);
		return f.parse(date);
	}
}
