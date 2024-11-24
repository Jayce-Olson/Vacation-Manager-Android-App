# Title: 
Vacation Manager

## Description:
This is a vacation manager app for android. It allows you users to schedule, edit, and delete vacations. Users can also schedule, edit, and delete excursions associated with
each vacation. The app takes advantage of a recyler view for efficient memory management and takes advantage of the Room Framework to simplify interactions with the SQLLite database.

img(App Layout)["app/App_Storyboard.png]

# Instructions:
The apps homepage will have a list of the already created vacations,
this will be clear to start.

## Add vacation: 
To add a vacation there will be a plus icon in the bottom
right hand corner of the screen, if you click that the "add vacation
screen" will appear. You will be unable to add Excursions, Delete, share, or set
notifications until the vacation is saved/created. After filling out the fields,
if they are valid, you can hit save and will be redirected to the homescreen 
where you will see your new vacation posted.

## Edit vacation:
To edit a vacation, click on it (The vacation item in the list) and you will be
directed to the edit vacation screen. Here you can you can:

#### Delete:
Click the menu button to the bottom right and click delete
#### Share:
Click the menu button to the bottom right and click share
#### Set Notifications:
Click the menu button to the bottom right and click Notify
#### Add Excursion:
Click the "Add Excursion" button. You will be directed to the "Add Excursion" screen where you can 
input the title and Date (will not go through without being in vacation range) and click save. You will
be unable to delete or set notifications until the Excursion is created.
#### Edit Excursion:
If an excursion already exists, you will see it listed below the edit vacation field in a scrollable
view, if you click the item in the list you will be directed to the edit excursion screen. Here you
will be able to edit the excursion, delete the excursion, and set a notification for the excursion. You
can also reach the "Edit Excursion" screen by clicking the excursion list item below the vacation list item
on the home screen.

# APK Deployed to:
minSdk 24
targetSdk 34
