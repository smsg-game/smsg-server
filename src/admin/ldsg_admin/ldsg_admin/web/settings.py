#coding=utf-8
# Django settings for from gba project.
from ldsg_admin.config import DjangoSettings, DEBUG
import os

TEMPLATE_DEBUG = DEBUG

#ADMINS = (
#    # ('Your Name', 'your_email@domain.com'),
#)
#
#MANAGERS = ADMINS

#DATABASE_ENGINE = DjangoSettings.DATABASE_ENGINE           # 'postgresql_psycopg2', 'postgresql', 'mysql', 'sqlite3' or 'oracle'.
#DATABASE_NAME = DjangoSettings.DATABASE_NAME             # Or path to database file if using sqlite3.
#DATABASE_USER = DjangoSettings.DATABASE_USER             # Not used with sqlite3.
#DATABASE_PASSWORD = DjangoSettings.DATABASE_PASSWORD         # Not used with sqlite3.
#DATABASE_HOST = DjangoSettings.DATABASE_HOST             # Set to empty string for localhost. Not used with sqlite3.
#DATABASE_PORT = DjangoSettings.DATABASE_PORT             # Set to empty string for default. Not used with sqlite3.

# Local time zone for this installation. Choices can be found here:
# http://en.wikipedia.org/wiki/List_of_tz_zones_by_name
# although not all choices may be available on all operating systems.
# If running in a Windows environment this must be set to the same as your
# system time zone.
TIME_ZONE = 'Asia/Chongqing'

# Language code for this installation. All choices can be found here:
# http://www.i18nguy.com/unicode/language-identifiers.html
LANGUAGE_CODE = 'zh-cn'

LOGIN_URL = '/login/'

SITE_ID = 1

# If you set this to False, Django will make some optimizations so as not
# to load the internationalization machinery.
USE_I18N = False

# Absolute path to the directory that holds media.
# Example: "/home/media/media.lawrence.com/"
MEDIA_ROOT = ''

# URL that handles the media served from MEDIA_ROOT. Make sure to use a
# trailing slash if there is a path component (optional in other cases).
# Examples: "http://media.lawrence.com", "http://example.com/media/"
MEDIA_URL = ''

# URL prefix for admin media -- CSS, JavaScript and images. Make sure to use a
# trailing slash.
# Examples: "http://foo.com/media/", "/media/".
ADMIN_MEDIA_PREFIX = '/media/'

# Make this unique, and don't share it with anybody.
SECRET_KEY = '4lf#_s6h25&m=f^20+gph4)@m98ilq=ag$%8f=gu3t_p2$+)d-'

# List of callables that know how to import templates from various sources.
TEMPLATE_LOADERS = (
    'django.template.loaders.filesystem.load_template_source',
    'django.template.loaders.app_directories.load_template_source',
#     'django.template.loaders.eggs.load_template_source',
)
#SESSION_ENGINE = "django.contrib.sessions.backends.cache"
MIDDLEWARE_CLASSES = (
    'ldsg_admin.common.exception_handler.ExceptionMiddleware',
    'ldsg_admin.common.jsonrpcserver.JSONRPCServerMiddleware',
    'django.middleware.common.CommonMiddleware',#加上这几行则可以避免上面的错误  
    'django.contrib.sessions.middleware.SessionMiddleware',  
    #'django.contrib.auth.middleware.AuthenticationMiddleware',  
)

ROOT_URLCONF = 'web.urls'

SETTINGS_CODE_FOLDER = os.path.dirname(os.path.realpath(__file__))
TEMPLATE_DIRS = (
    # Put strings here, like "/home/html/django_templates" or "C:/www/django/templates".
    # Always use forward slashes, even on Windows.
    # Don't forget to use absolute paths, not relative paths.
    os.path.join(SETTINGS_CODE_FOLDER, "templates").replace("\\", "/"),
)

STATIC_PATH = os.path.join(SETTINGS_CODE_FOLDER, "media").replace("\\", "/")
CAPTCHA_PATH = os.path.join(STATIC_PATH, 'captcha')       # 验证码文件目录
CAPTCHA_M = 'efa1f06ee6a611dd8afa005056c00008' #uuid.uuid1().get_hex()

INSTALLED_APPS = (
    #'django.contrib.contenttypes',
#    'django.contrib.sessions',
    'web',
)

URL_PREFIX = DjangoSettings.URL_PREFIX

TEMPLATE_CONTEXT_PROCESSORS = (
    'ldsg_admin.business.context_processors.set_server_list',
   'django.core.context_processors.request',
)

#-------------------------
FILE_UPLOAD_HANDLERS = (
#    "django.core.files.uploadhandler.MemoryFileUploadHandler",
    "django.core.files.uploadhandler.TemporaryFileUploadHandler",
)
FILE_UPLOAD_TEMP_DIR = DjangoSettings.FILE_UPLOAD_TEMP_DIR
#-------------------------------------------------

FORCE_SCRIPT_NAME = '' # http://docs.djangoproject.com/en/dev/howto/deployment/fastcgi/#lighttpd-setup

WEB_ROOT = DjangoSettings.WEB_ROOT # http://superjared.com/projects/static-generator/