// Copyright The OpenTelemetry Authors
// SPDX-License-Identifier: Apache-2.0

package loggingexporter

import (
	"path/filepath"
	"testing"

	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/require"
	"go.uber.org/zap/zapcore"

	"go.opentelemetry.io/collector/config/configtelemetry"
	"go.opentelemetry.io/collector/confmap"
	"go.opentelemetry.io/collector/confmap/confmaptest"
)

func TestUnmarshalDefaultConfig(t *testing.T) {
	factory := NewFactory()
	cfg := factory.CreateDefaultConfig()
	require.NoError(t, confmap.New().Unmarshal(&cfg))
	assert.Equal(t, factory.CreateDefaultConfig(), cfg)
}

func TestUnmarshalConfig(t *testing.T) {
	tests := []struct {
		filename    string
		cfg         *Config
		expectedErr string
	}{
		{
			filename: "config_loglevel.yaml",
			cfg: &Config{
				LogLevel:           zapcore.DebugLevel,
				Verbosity:          configtelemetry.LevelDetailed,
				SamplingInitial:    10,
				SamplingThereafter: 50,
				warnLogLevel:       true,
			},
		},
		{
			filename: "config_verbosity.yaml",
			cfg: &Config{
				LogLevel:           zapcore.InfoLevel,
				Verbosity:          configtelemetry.LevelDetailed,
				SamplingInitial:    10,
				SamplingThereafter: 50,
			},
		},
		{
			filename: "loglevel_info.yaml",
			cfg: &Config{
				LogLevel:           zapcore.InfoLevel,
				Verbosity:          configtelemetry.LevelNormal,
				SamplingInitial:    2,
				SamplingThereafter: 500,
				warnLogLevel:       true,
			},
		},
		{
			filename:    "invalid_verbosity_loglevel.yaml",
			expectedErr: "'loglevel' and 'verbosity' are incompatible. Use only 'verbosity' instead",
		},
		{
			filename:    "config_loglevel_typo.yaml",
			expectedErr: "'' has invalid keys: logLevel",
		},
	}

	for _, tt := range tests {
		t.Run(tt.filename, func(t *testing.T) {
			cm, err := confmaptest.LoadConf(filepath.Join("testdata", tt.filename))
			require.NoError(t, err)
			factory := NewFactory()
			cfg := factory.CreateDefaultConfig()
			err = cm.Unmarshal(&cfg)
			if tt.expectedErr != "" {
				assert.ErrorContains(t, err, tt.expectedErr)
			} else {
				require.NoError(t, err)
				assert.Equal(t, tt.cfg, cfg)
			}
		})
	}
}

func Test_UnmarshalMarshalled(t *testing.T) {
	for name, tc := range map[string]struct {
		inCfg          *Config
		expectedConfig *Config
		expectedErr    string
	}{
		"Base": {
			inCfg:          &Config{},
			expectedConfig: &Config{},
		},
		"VerbositySpecified": {
			inCfg: &Config{
				Verbosity: configtelemetry.LevelDetailed,
			},
			expectedConfig: &Config{
				Verbosity: configtelemetry.LevelDetailed,
			},
		},
		"LogLevelSpecified": {
			inCfg: &Config{
				LogLevel: zapcore.DebugLevel,
			},
			expectedConfig: &Config{
				LogLevel:     zapcore.DebugLevel,
				Verbosity:    configtelemetry.LevelDetailed,
				warnLogLevel: true,
			},
		},
		"SpecifiedLogLevelExpectedErr": {
			inCfg: &Config{
				// Cannot specify both log level and verbosity so an error is expected
				LogLevel:  zapcore.DebugLevel,
				Verbosity: configtelemetry.LevelNormal,
			},
			expectedErr: "'loglevel' and 'verbosity' are incompatible. Use only 'verbosity' instead",
		},
	} {
		t.Run(name, func(t *testing.T) {

			conf := confmap.New()
			err := conf.Marshal(tc.inCfg)
			require.NoError(t, err)

			raw := conf.ToStringMap()

			conf = confmap.NewFromStringMap(raw)

			outCfg := &Config{}

			err = conf.Unmarshal(outCfg)

			if tc.expectedErr == "" {
				require.NoError(t, err)
				assert.Equal(t, tc.expectedConfig, outCfg)
				return
			}
			require.Error(t, err)
			assert.EqualError(t, err, tc.expectedErr)
		})
	}
}

func TestValidate(t *testing.T) {
	tests := []struct {
		name        string
		cfg         *Config
		expectedErr string
	}{
		{
			name: "verbosity none",
			cfg: &Config{
				Verbosity: configtelemetry.LevelNone,
			},
			expectedErr: "verbosity level \"None\" is not supported",
		},
		{
			name: "verbosity detailed",
			cfg: &Config{
				Verbosity: configtelemetry.LevelDetailed,
			},
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			err := tt.cfg.Validate()
			if tt.expectedErr != "" {
				assert.EqualError(t, err, tt.expectedErr)
			} else {
				assert.NoError(t, err)
			}
		})
	}
}
