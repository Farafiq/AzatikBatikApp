const storageBatik = async (req, res) => {
    try {
        const id = await id.findAll({
            where: {
                id: req.params.id
            }
        });
        res.json(id[0]);
    } catch (error) {
        res.json({
            message: error.message
        });
    }
}

const uploadBatik = async (req, res) => {
    res.json({
    status: 'success',
    message: null,
    data: {}
    });
};


const deleteBatik = async (req, res) => {
    try {
        await id.destroy({
            where: {
                id: req.params.id,
            }
        })
    } catch (error) {
        res.json({
            message: error.message
        });
    }
};


export { storageBatik, uploadBatik, deleteBatik };