#The file interface.py contains the code for the GUI
#pip install PyQt5
from PyQt5 import QtWidgets
from PyQt5 import uic
from PyQt5.QtWidgets import *
from PyQt5.QtGui import *
from PyQt5.QtCore import *
import sys

class ScrollableLable(QScrollArea):

    def _init_(self, *args, **kwargs):
        QScrollArea._init_(self, *args, **kwargs)
        widget = QWidget(self)
        self.setWidgetResizable(True)
        self.setWidget(widget)
        layout = QVBoxLayout(widget)     #not necessary?
        self.label.setAlignment(Qt.AlignLeft | Qt.AlignTop)
        self.label.setFont(QFont('Arial', 15))
        self.label.setStyleSheet("background-color: beige; border: 1px solid black;")
        layout.addWidget(self.label)

    def setText(self, text):
        self.label.setText(text)

class MyWindow(QMainWindow):
    def _init_(self):
        super(MyWindow, self)._init_()
        self.setGeometry(300, 50, 1600, 900)
        self.setWindowTitle("Application GUI")

        # Output text for query and annotation
        self.queryOutput = ScrollableLable(self)
        self.queryAnnotate = ScrollableLable(self)

        # Button for running algorithm
        self.submitButton = QtWidgets.QPushButton(self)

        # Textbox for query and db name
        self.queryTextbox = QTextEdit(self)
        self.dbNameTextbox = QTextEdit(self)

        # Label to indicate which db name is the app currently connected to
        self.dbNameLabel = ScrollableLable(self)

        # Error message box
        self.error_dialog = QtWidgets.QErrorMessage()

        self.initUI()

        # Db connection settings 
        self.connection = None
        self.dbName = ''

    #def initUI(self):
        