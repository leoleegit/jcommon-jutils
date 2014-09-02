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
package org.jcommon.com.util.share;

import java.util.Map;

public interface ShareListener {
    public void onAddObject(String request, boolean sResult);
	
	public void onHasObject(String request, boolean sResult);
	
	public void onUpdateObject(String request, boolean sResult);
	
	public void onGetObject(String request, Object sResult);
	
	public void onRemoveObject(String request, boolean sResult);
	
	public void onGetAllObject(String request, Map<Object, Object> all);
	
	public void onException(String request, Exception e);
}
