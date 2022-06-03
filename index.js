import express from 'express';
import cors from 'cors';
import batikRoutes from './routes/batik.js';
import scanRoutes from './routes/scan.js';

const app = express();

app.use(express.json());
app.use(express.urlencoded({ extended: true }));
app.use(cors());

app.use('/batik', batikRoutes);
app.use('/scan', scanRoutes);


const port = process.env.PORT || 5000;
app.listen(port, () => console.log(`Server running on ${port}` ));

