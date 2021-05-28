import os
import json

from azureml.studio.core.io.model_directory import ModelDirectory
from pathlib import Path
from azureml.studio.modules.ml.score.score_generic_module.score_generic_module import ScoreModelModule
from azureml.designer.serving.dagengine.converter import create_dfd_from_dict
from collections import defaultdict
from azureml.designer.serving.dagengine.utils import decode_nan
from azureml.studio.common.datatable.data_table import DataTable

model_path = os.path.join(os.getenv('AZUREML_MODEL_DIR'), 'trained_model_outputs')
schema_file_path = Path(model_path) / '_schema.json'
with open(schema_file_path) as fp:
    schema_data = json.load(fp)


def init():
    global model
    model = ModelDirectory.load(model_path).model


def run(data):
    data = json.loads(data)
    input_entry = defaultdict(list)
    for row in data:
        for key, val in row.items():
            input_entry[key].append(decode_nan(val))

    data_frame_directory = create_dfd_from_dict(input_entry, schema_data)
    score_module = ScoreModelModule()
    result, = score_module.run(
        learner=model,
        test_data=DataTable.from_dfd(data_frame_directory),
        append_or_result_only=True)

    grouped = result.data_frame.groupby("Scored Labels")
    groups = None
    for name, group in grouped:
        new_group = group.sort_values(by=['Scored Probabilities_' + name], ascending=False)
        if groups is None:
            groups = new_group.head(5)
        else:
            groups = groups.append(new_group.head(5))

    return {"result": groups.to_dict(orient="records") }

