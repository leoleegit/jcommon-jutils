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

import java.util.Set;

public class SortUtils {
	
	 public static int[] maoPao(int[] x) {
		 for (int i = 0; i < x.length; i++) {
			  for (int j = i + 1; j < x.length; j++) {
			    if (x[i] > x[j]) {
			       int temp = x[i];
			       x[i] = x[j];
			       x[j] = temp;
			    }
			  }
		 }
		 return x;
	 }

	 public static String[] sortKey(Set<String> keys){
		 String[] keys_ =  new String[keys.size()];
		 System.arraycopy(keys.toArray(), 0, keys_, 0, keys.size());
		 for(int i=0; i<keys.size(); i++){
			 for (int j = i + 1; j < keys.size(); j++) {
			      int x_i = Integer.valueOf(keys_[i]);
			      int x_j = Integer.valueOf(keys_[j]);
			      if (x_i > x_j) {
				       String temp = keys_[i];
				       keys_[i] = keys_[j];
				       keys_[j] = temp;
				   }
			 }
		 }
		 return keys_;
	 }
	 
	 public static int[] xuanZe(int[] x) {
		for (int i = 0; i < x.length; i++) {
		   int lowerIndex = i;
		   for (int j = i + 1; j < x.length; j++) {
		      if (x[j] < x[lowerIndex]) {
		          lowerIndex = j;
		      }
		   }
		   int temp = x[i];
		   x[i] = x[lowerIndex];
		   x[lowerIndex] = temp;
		}
		return x;
	 }
}


