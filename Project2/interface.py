from PyQt5 import QtWidgets
from PyQt5.QtWidgets import *
from PyQt5.QtGui import *
from PyQt5.QtCore import *
import sys
from project import *
from explain import *
import traceback

# from annotation import process, init_conn


class ScrollableLabel(QScrollArea):

    def __init__(self, *args, **kwargs):
        QScrollArea.__init__(self, *args, **kwargs)
        widget = QWidget(self)
        self.setWidgetResizable(True)
        self.setWidget(widget)
        layout = QVBoxLayout(widget)
        self.label = QLabel(widget)
        self.label.setAlignment(Qt.AlignLeft | Qt.AlignTop)
        self.label.setFont(QFont('Arial', 12))
        self.label.setStyleSheet(
            "background-color: beige; border: 1px solid black;")

        layout.addWidget(self.label)

    def setText(self, text):
        self.label.setText(text)


class MyWindow(QMainWindow):
    def __init__(self):
        super(MyWindow, self).__init__()
        self.setGeometry(50, 50, 1800, 1000)  # xpos, ypos, width, height
        self.setWindowTitle("CZ4031-Assignment2-Group 31")

        # Output text for query and annotation
        self.queryOutput1 = ScrollableLabel(self)
        self.queryOutput2 = ScrollableLabel(self)
        self.queryExplain = ScrollableLabel(self)

        # Button for running algorithm
        self.submitButton = QtWidgets.QPushButton(self)

        # Textbox for query and db name
        self.queryTextbox1 = QTextEdit(self)
        self.queryTextbox2 = QTextEdit(self)
        self.dbNameTextbox = QTextEdit(self)

        # Label to indicate which db name is the app currently connected to
        self.dbNameLabel = ScrollableLabel(self)

        # Error message box
        self.error_dialog = QtWidgets.QErrorMessage()

        self.UI()  # Call initUI

        # Db connection settings
        self.database = None
        self.dbName = ''

        self.refreshButton = QtWidgets.QPushButton(self)
        self.refreshButton.setText("Refresh")
        self.refreshButton.setFont(QFont('Arial', 12))
        self.refreshButton.clicked.connect(self.onRefresh)
        self.refreshButton.move(1500, 820)
        self.refreshButton.resize(270, 100)

    def UI(self):
        self.queryTextbox1.move(30, 5)
        self.queryTextbox1.resize(700, 150)
        self.queryTextbox1.setFont(QFont('Arial', 12))
        self.queryTextbox1.setPlaceholderText("Insert SQL Query Q1 Here :")

        self.queryTextbox2.move(740, 5)
        self.queryTextbox2.resize(700, 150)
        self.queryTextbox2.setFont(QFont('Arial', 12))
        self.queryTextbox2.setPlaceholderText("Insert SQL Query Q2 Here :")

        self.dbNameTextbox.move(1500, 5)
        self.dbNameTextbox.resize(270, 150)
        self.dbNameTextbox.setFont(QFont('Arial', 12))
        self.dbNameTextbox.setPlaceholderText("Insert Database Name Here :")

        self.queryOutput1.setText("Output Query Q1 :")
        self.queryOutput1.move(30, 170)
        self.queryOutput1.resize(700, 400)
        self.queryOutput1.setHorizontalScrollBarPolicy(Qt.ScrollBarAlwaysOn)
        self.queryOutput1.setVerticalScrollBarPolicy(Qt.ScrollBarAlwaysOn)

        self.queryOutput2.setText("Output Query Q2 :")
        self.queryOutput2.move(740, 170)
        self.queryOutput2.resize(700, 400)
        self.queryOutput2.setHorizontalScrollBarPolicy(Qt.ScrollBarAlwaysOn)
        self.queryOutput2.setVerticalScrollBarPolicy(Qt.ScrollBarAlwaysOn)

        self.queryExplain.setText("Explain for Output Query :")
        self.queryExplain.move(30, 580)
        self.queryExplain.resize(1410, 400)
        self.queryExplain.setHorizontalScrollBarPolicy(Qt.ScrollBarAlwaysOn)
        self.queryExplain.setVerticalScrollBarPolicy(Qt.ScrollBarAlwaysOn)

        self.queryOutput1.verticalScrollBar().valueChanged.connect(
            self.queryExplain.verticalScrollBar().setValue)
        self.queryExplain.verticalScrollBar().valueChanged.connect(
            self.queryOutput1.verticalScrollBar().setValue)

        self.queryOutput2.verticalScrollBar().valueChanged.connect(
            self.queryExplain.verticalScrollBar().setValue)
        self.queryExplain.verticalScrollBar().valueChanged.connect(
            self.queryOutput2.verticalScrollBar().setValue)

        self.dbNameLabel.move(1500, 170)
        self.dbNameLabel.resize(270, 100)
        self.dbNameLabel.setText(f"Current Database Name: ")

        self.submitButton.setText("Submit Query")
        self.submitButton.setFont(QFont('Arial', 12))
        self.submitButton.clicked.connect(self.onClick)
        self.submitButton.move(1500, 700)
        self.submitButton.resize(270, 100)

    def onRefresh(self):
        self.queryTextbox1.clear()
        self.queryTextbox2.clear()
        self.queryOutput1.setText("Output Query Q1 :")
        self.queryOutput2.setText("Output Query Q2 :")
        self.queryExplain.setText("Explain for Output Query :")

    def onClick(self):
        if self.dbName != self.dbNameTextbox.toPlainText():
            try:
                # A connection in need of explain.py's obtain message function
                self.database = Database(self.dbNameTextbox.toPlainText())
                self.dbName = self.dbNameTextbox.toPlainText()
                self.dbNameLabel.setText(f"Current DB Name: {self.dbName}")
            except Exception as e:
                self.error_dialog.showMessage(f"ERROR - {e}")
        if self.database is not None:
            try:

                explain_class = explain()
                query, annotation = process(
                    self.database, self.queryTextbox1.toPlainText())
                query2, annotation = process(
                    self.database, self.queryTextbox2.toPlainText())
                explanation = explain_class.explain(
                    self.database, self.queryTextbox1.toPlainText(), self.queryTextbox2.toPlainText())
                self.queryExplain.setText(explanation)
                # print(query)
                print(type(query))
                query_str = ''
                query_str2 = ''
                j=0
                for x in query:
                    query_str += '\n' + x
                    if len(query_str)>1000:
                        break
                for x in query2:
                    query_str2 += '\n' + x
                    if len(query_str2)>1000:
                        break

                self.queryOutput1.setText(query_str)
                self.queryOutput2.setText(query_str2)

            except Exception as e:
                traceback.print_exc()
                self.error_dialog.showMessage(f"ERROR - {e}")


def process(database, query):
    query_results = database.get_query_results(query)
    final_query_result = []
    for i in query_results:
        temp = ""
        for j in i:
            temp += str(j)+"|"
        final_query_result.append(temp)
    raw_explanation = database.get_query_results(
        "explain (analyze true , format json)" + query)


    final_raw_explanation = []
    for i in raw_explanation:
        final_raw_explanation.append(i[0])
    return final_query_result, final_raw_explanation



def window():
    app = QApplication(sys.argv)
    win = MyWindow()

    win.show()
    sys.exit(app.exec_())


def main():
    window()


if __name__ == '__main__':
    main()
