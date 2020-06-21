from django.contrib import admin

# Register your models here.

from UserModel.models import Student,Teacher,studentKeyword,teacherKeyword,follow,message

# Register your models here.
admin.site.register(Student)
admin.site.register(Teacher)
admin.site.register(studentKeyword)
admin.site.register(teacherKeyword)
admin.site.register(follow)
admin.site.register(message)