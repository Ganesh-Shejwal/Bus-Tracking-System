package com.health.conductorapp;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StudentListAdapter extends RecyclerView.Adapter<StudentListAdapter.ViewHolder> {
    Context context;
    ArrayList<HashMap<String, String>> studentList;
    StudentList studentList1;

    public StudentListAdapter(Context context, ArrayList<HashMap<String, String>> studentList, StudentList studentList1) {
        this.context = context;
        this.studentList = studentList;
        this.studentList1 = studentList1;
    }

    @NonNull
    @Override
    public StudentListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_list_item_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentListAdapter.ViewHolder holder, int position) {
        HashMap<String, String> hm = studentList.get(position);
        holder.txtRollNumber.setText("" + hm.get("roll_number"));
        holder.txtStudentName.setText("" + hm.get("student_name"));
        holder.txtEmail.setText("" + hm.get("student_email"));
        holder.txtContactNumber.setText("" + hm.get("student_mob"));
        holder.txtAddress.setText("" + hm.get("address"));
        holder.txtMotherName.setText("" + hm.get("mother_name"));

        holder.txtViewQrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ViewQR.class);
                intent.putExtra("imageUrl", hm.get("image_path"));
                context.startActivity(intent);

            }
        });

        holder.txtEditStudents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showPopUp(view, hm.get("roll_number"), hm.get("student_name"), hm.get("student_email"), hm.get("student_mob"), hm.get("address"), hm.get("mother_name"),studentList1);

            }
        });


        holder.txtDeleteStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                studentList1.deleteStudent(hm.get("roll_number"));

            }
        });



    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtRollNumber, txtStudentName, txtEmail, txtContactNumber, txtAddress, txtMotherName, txtViewQrCode, txtEditStudents,txtDeleteStudent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtRollNumber = itemView.findViewById(R.id.txt_roll_number);
            txtStudentName = itemView.findViewById(R.id.txt_student_name);
            txtEmail = itemView.findViewById(R.id.txt_email);
            txtContactNumber = itemView.findViewById(R.id.txt_contact_number);
            txtAddress = itemView.findViewById(R.id.txt_address);
            txtMotherName = itemView.findViewById(R.id.txt_mother_name);
            txtViewQrCode = itemView.findViewById(R.id.txt_view_qr_code);
            txtEditStudents = itemView.findViewById(R.id.txt_edit_studnts);
            txtDeleteStudent = itemView.findViewById(R.id.txt_delete_studnts);


        }
    }


    public void showPopUp(View view, String strRollNumber, String strStudentName, String strStudentEmail, String strContactNumber, String strAddress, String strMotherName,StudentList studentList2) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.edit_student_popup);
        dialog.setTitle("Edit Students Record");

        TextView rollNumber = (TextView) dialog.findViewById(R.id.txt_popup_roll_number);
        EditText studentName = (EditText) dialog.findViewById(R.id.edit_student_name);
        EditText studentEmail = (EditText) dialog.findViewById(R.id.edit_student_email);
        EditText contactNumber = (EditText) dialog.findViewById(R.id.edit_contact);
        EditText address = (EditText) dialog.findViewById(R.id.edit_address);
        EditText motherName = (EditText) dialog.findViewById(R.id.edit_mother_name);
        Button updateStudent = dialog.findViewById(R.id.btn_update);

        rollNumber.setText("" + strRollNumber);
        studentName.setText("" + strStudentName);
        studentEmail.setText("" + strStudentEmail);
        contactNumber.setText("" + strContactNumber);
        address.setText("" + strAddress);
        motherName.setText("" + strMotherName);

        // if button is clicked, close the custom dialog
        updateStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(studentName.getText().toString().matches("^[a-zA-Z]*$")&&motherName.getText().toString().matches("^[a-zA-Z]*$")&&contactNumber.getText().toString().length()==10)
                {
                    dialog.dismiss();
                    updateStudent(rollNumber.getText().toString(),studentName.getText().toString(),studentEmail.getText().toString(),contactNumber.getText().toString(),address.getText().toString(),motherName.getText().toString(),studentList2);

                }
                else {
                    Toast.makeText(studentList2, "Please Enter Valid Details..", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();

    }


    public void updateStudent(String strRollNumber, String strStudentName, String strStudentEmail, String strContactNumber, String strAddress, String strMotherName,StudentList studentList2)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.UPDATE_STUDENTS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.i("responseData", "" + response);

                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    String msg = jsonObject.getString("responseMessage");
                    if (code.equals("1")) {
                        Toast.makeText(context, "" + msg, Toast.LENGTH_SHORT).show();
                        studentList2.callApi();

                    } else {
                        Toast.makeText(context, "" + msg, Toast.LENGTH_SHORT).show();

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i("exception", "" + e);
                    Toast.makeText(context, "PLEASE TRY AFTER SOME TIME", Toast.LENGTH_SHORT).show();

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("error", "" + error);
                Toast.makeText(context, "PLEASE TRY AFTER SOME TIME", Toast.LENGTH_SHORT).show();

            }
        }) {

            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("rollNumber", strRollNumber);
                hashMap.put("studentName", strStudentName);
                hashMap.put("contactNumber", strContactNumber);
                hashMap.put("address", strAddress);
                hashMap.put("motherName", strMotherName);
                hashMap.put("email", strStudentEmail);
                return hashMap;
            }
        };


        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

    }
}
