from PIL import Image
from django.core.files.base import ContentFile
from django.http import HttpResponse
from django.shortcuts import render, get_object_or_404


def hello(request):
    return HttpResponse("hello")