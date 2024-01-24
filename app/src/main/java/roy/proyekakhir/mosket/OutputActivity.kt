package roy.proyekakhir.mosket

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

class OutputActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_output)

        val byteArray = intent.getByteArrayExtra("image")
        val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray!!.size)
        val nama = intent.getStringExtra("nama")

        val deskripsi: TextView = findViewById(R.id.txtdeskripsi)
        val motif: TextView = findViewById(R.id.motif)
        val gambar: ImageView = findViewById(R.id.foto)
        val btnKembali: Button = findViewById(R.id.btkembali)


        motif.text = "Nama Motif: " + intent.getStringExtra("motif")

        gambar.setImageBitmap(bitmap)

        btnKembali.setOnClickListener {
            onBackPressed()
        }
        if (nama == "Lebah Begayut") {
            deskripsi.text = "Motif ini melambangkan kerja keras, keteraturan, dan produktivitas. Lebah adalah makhluk yang rajin mengumpulkan nektar dan madu dengan tekun" +
                    "Motif ini mengingatkan kita akan pentingnya usaha dan kerja keras dalam mencapai tujuan, serta nilai-nilai ketekunan dalam menjalani kehidupan sehari-hari"
        }
        else if(nama == "Pucuk Rebung"){
            deskripsi.text= "Motif ini menggambarkan sebuah pucuk bambu yang runcing dan dikelilingi " +
                    "dengan daun-daun sehingga berbentuk segitiga dan ujungnya yang runcing " +
                    "terlihat seperti ujung pedang. Motif ini melambangkan kesabaran, kesuburan " +
                    "dan tekad hati dalam mencapai suatu tujuan dan harapan. Pohon bambu mewakili " +
                    "tekad hati yang tidak mudah roboh dan jatuh bahkan ketika tertiup angin kencang."
        }
        else if(nama == "Siku Awan"){
            deskripsi.text= "Mmenggambarkan awan yang melayang dengan lembut, melambangkan kelembutan, kedamaian, dan ketenangan. Awan sering dihubungkan dengan suasana yang menenangkan."
        }
        else if(nama == "Siku Keluang"){
            deskripsi.text= "Motif ini digambarkan seperti sudut-sudut sayap kelelawar " +
                    "yang menggambarkan tanggung jawab. Motif ini juga diartikan " +
                    "memiliki sifat yang amanah dan teguh pendirian."
        }
        else {
            deskripsi.text= "Motif ini menggambarkan buah manggis yang terbelah tepat di bagian tengahnya " +
                    "dan menampakannya kulit dan isi buah. Motif ini memiliki makna sopan santun, " +
                    "budi pekerti dan manis. Motif ini juga dapat dimaknai sebagai kebaikan seseorang " +
                    "tidak dapat dinilai dari kulitnya."
        }
    }
}