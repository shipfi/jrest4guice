// <#-- 此处代码在服务端校验时忽略
function validatePasswordFields (form) {
	return true;
}

function validatePermissionFields (form) {
	return true;
}

function validateUserFields (form) {
	return true;
}
// -->

// <#if action.model='user'><#assign validateForm>
function validateUserForm () {
	if (!validateUserFields(this)) {
		return false;
	}
	if (this['model.password'].value != this['model.confirmPassword'].value) {
		alert('密码与确认密码必须一致！');
		return false;
	}
	return true;
}
// </#assign></#if>

// <#if action.model='password'><#assign validateForm>
function validatePasswordForm () {
	if (!validatePasswordFields(this)) {
		return false;
	}
	if (this['model.password'].value != this['model.confirmPassword'].value) {
		alert('密码与确认密码必须一致！');
		return false;
	}
	return true;
}
// </#assign></#if>

// <#if action.model='permission'><#assign validateForm>
function validatePermissionForm () {
	if (!validatePermissionFields(this)) {
		return false;
	}
	if (this['model.authority'].value.indexOf('AUTH_') != 0) {
		alert('权限必须以AUTH_开头！');
		return false;
	}
	return true;
}
// </#assign></#if>