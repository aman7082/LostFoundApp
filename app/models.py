from django.db import models
from django.contrib.auth.models import User

class Item(models.Model):
    ITEM_STATUS = (
        ('lost', 'Lost'),
        ('found', 'Found'),
        ('claimed', 'Claimed'),
    )
    
    title = models.CharField(max_length=100)
    description = models.TextField()
    category = models.CharField(max_length=50)
    location = models.CharField(max_length=100)
    date = models.DateField()
    status = models.CharField(max_length=10, choices=ITEM_STATUS)
    image = models.ImageField(upload_to='items/', blank=True)
    reporter = models.ForeignKey(User, on_delete=models.CASCADE)
    
    def __str__(self):
        return self.title