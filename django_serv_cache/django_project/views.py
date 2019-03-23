import base64
import json
from django.contrib import auth
from django.contrib.auth.models import User
from django.http import HttpResponse
from django.utils import timezone
from django.shortcuts import render, get_object_or_404, redirect

def auth(request):
    return render(request, "django_project/auth.html", {})
